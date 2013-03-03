//
//  TVC_ScheduleTeamSelector.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 3/2/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "TVC_ScheduleTeamSelector.h"
#import "MemoryManager.h"
#import "Team.h"
#import "ScheduleGamesViewController.h"

@interface TVC_ScheduleTeamSelector ()

@property (strong, nonatomic) NSArray *arrayOfTeamArrays;

@end

@implementation TVC_ScheduleTeamSelector

@synthesize teamsArray,leagueArray;

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    MemoryManager *myManger = [[MemoryManager alloc]init];
    teamsArray = [myManger getTeamArray];
    leagueArray = [self generateLeagueArray];
    self.arrayOfTeamArrays = [self generateDoubleArray];
}

-(NSArray *) generateDoubleArray {
    NSMutableArray *mutable = [[NSMutableArray alloc]init];
    for(int i=0;i<leagueArray.count;i++)
        [mutable addObject:[[NSMutableArray alloc]init]];
    
    for(int i=0;i<teamsArray.count;i++)
        for(int j=0;j<leagueArray.count;j++)
            if([((Team *)[teamsArray objectAtIndex:i]).league isEqualToString:((NSString *) [leagueArray objectAtIndex:j])])
                if(![[mutable objectAtIndex:j] containsObject:((Team *) [teamsArray objectAtIndex:i])])
                    [[mutable objectAtIndex:j] addObject:((Team *) [teamsArray objectAtIndex:i])];
    return [[NSArray alloc] initWithArray:mutable];
}

-(int)getNumberOfTeamsInLeague:(NSString *)league {
    int count=0;
    for(int i=0;i<teamsArray.count;i++) {
        Team *temp = [teamsArray objectAtIndex:i];
        if([temp.league isEqualToString:league])
            count++;
    }
    return count;
}

-(NSArray *)generateLeagueArray {
    NSMutableArray *mutable = [[NSMutableArray alloc]init];
    
    for(int i=0;i<teamsArray.count;i++) {
        Team *temp = [teamsArray objectAtIndex:i];
        
        if(![mutable containsObject:temp.league])
            [mutable addObject:temp.league];
    }
    return [[NSArray alloc] initWithArray:mutable];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return leagueArray.count;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSString *leagueName = [leagueArray objectAtIndex:section];
    return [self getNumberOfTeamsInLeague:leagueName];
}

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    return [leagueArray objectAtIndex:section];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"scheduleTeamSelectorCell";
    UITableViewCell *cell;
    cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    UILabel *label = (UILabel *)[cell viewWithTag:0];
    
    Team *temp = [[self.arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    
    label.text = [temp teamName];
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    ScheduleGamesViewController *scheduleGamesViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"ScheduleGamesViewController"];
    scheduleGamesViewController.dataToDisplay = [((Team *) [[self.arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row]) getTeamKey];
    [self.navigationController pushViewController:scheduleGamesViewController animated:YES];
}

@end
