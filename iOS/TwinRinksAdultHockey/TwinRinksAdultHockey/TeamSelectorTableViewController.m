//
//  TeamSelectorTableViewController.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 3/2/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "TeamSelectorTableViewController.h"
#import "MemoryManager.h"
#import "Team.h"

@interface TeamSelectorTableViewController ()

@property (strong, nonatomic) NSArray *arrayOfTeamArrays;

@end

@implementation TeamSelectorTableViewController

@synthesize teamsArray,leagueArray,arrayOfTeamArrays;

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
    arrayOfTeamArrays = [self generateDoubleArray];
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

-(NSArray *)generateLeagueArray {
    NSMutableArray *mutable = [[NSMutableArray alloc]init];
    
    for(int i=0;i<teamsArray.count;i++) {
        Team *temp = [teamsArray objectAtIndex:i];
        
        if(![mutable containsObject:temp.league])
            [mutable addObject:temp.league];
    }
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

-(void)removeTeamFromArrayWithName:(NSString *)teamNameP andLeague:(NSString *)leagueP {
    NSMutableArray *mutable = [NSMutableArray arrayWithArray:teamsArray];
    for(int i=mutable.count -1;i>=0;i--) {
        Team *temp = [mutable objectAtIndex:i];
        if([temp.teamName isEqualToString:teamNameP] && [temp.league isEqualToString:leagueP])
            [mutable removeObjectAtIndex:i];
    }
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

- (NSString *)tableView:(UITableView *)tableView titleForHeaderInSection:(NSInteger)section
{
    // set the section title to the matching letter
    return [leagueArray objectAtIndex:section];
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    NSString *leagueName = [leagueArray objectAtIndex:section];
    return [self getNumberOfTeamsInLeague:leagueName];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"teamSelectorCell";
    UITableViewCell *cell;
    cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    UILabel *label = (UILabel *)[cell viewWithTag:0];
    
    Team *temp = [[arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    
    label.text = [temp teamName];
    return cell;
}



#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    
    MemoryManager *myManger = [[MemoryManager alloc]init];
    NSMutableArray *yourTeamsArray = [NSMutableArray arrayWithArray:[myManger getYourTeamArray]];
    Team *toAdd = [[arrayOfTeamArrays objectAtIndex:indexPath.section] objectAtIndex:indexPath.row];
    [yourTeamsArray addObject:toAdd];
    [myManger saveYourTeamsArray:yourTeamsArray];
    

    UIViewController *settingsViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"SettingsViewController"];
    [self.navigationController pushViewController:settingsViewController animated:YES];
    [self performSegueWithIdentifier:@"SelectorToSettings" sender:self];
}

@end
