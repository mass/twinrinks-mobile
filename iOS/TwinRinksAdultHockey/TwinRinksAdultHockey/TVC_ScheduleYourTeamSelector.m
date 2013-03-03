//
//  TVC_ScheduleYourTeamSelector.m
//  TwinRinksAdultHockey
//
//  Created by Andrew on 3/2/13.
//  Copyright (c) 2013 GigaStorm. All rights reserved.
//

#import "TVC_ScheduleYourTeamSelector.h"
#import "MemoryManager.h"
#import "Team.h"
#import "ScheduleGamesViewController.h"

@interface TVC_ScheduleYourTeamSelector ()

@end

@implementation TVC_ScheduleYourTeamSelector

@synthesize yourTeamsArray;

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
    yourTeamsArray = [myManger getYourTeamArray];

}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

#pragma mark - Table view data source

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    // Return the number of sections.
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    // Return the number of rows in the section.
    return yourTeamsArray.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    static NSString *cellIdentifier = @"scheduleYourTeamSelectorCell";
    UITableViewCell *cell;
    cell = [tableView dequeueReusableCellWithIdentifier:cellIdentifier forIndexPath:indexPath];
    
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:cellIdentifier];
    }
    
    UILabel *label = (UILabel *)[cell viewWithTag:0];
    
    Team *temp = [yourTeamsArray objectAtIndex:indexPath.row];
    
    label.text = [temp toString];
    return cell;
}

#pragma mark - Table view delegate

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    ScheduleGamesViewController *scheduleGamesViewController = [self.storyboard instantiateViewControllerWithIdentifier:@"ScheduleGamesViewController"];
    scheduleGamesViewController.dataToDisplay = [((Team *) [yourTeamsArray objectAtIndex:indexPath.row]) getTeamKey];
    [self.navigationController pushViewController:scheduleGamesViewController animated:YES];
}

@end
